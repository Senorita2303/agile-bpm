package com.dstz.org.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.constats.ThreadMapKeyConstant;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.utils.CastUtils;
import com.dstz.base.query.ConditionType;
import com.dstz.base.query.QuerySort;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.core.entity.Group;
import com.dstz.org.core.entity.Role;
import com.dstz.org.core.manager.GroupManager;
import com.dstz.org.core.manager.OrgPostManager;
import com.dstz.org.core.manager.OrgRelationManager;
import com.dstz.org.core.manager.RoleManager;
import com.dstz.org.core.mapper.OrgRelationMapper;
import com.dstz.org.enums.RelationTypeConstant;
import com.dstz.org.vo.GroupVO;
import com.dstz.org.vo.OrgPostVO;
import com.google.common.collect.ImmutableMap;
import org.checkerframework.common.value.qual.IntRange;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Group service interface adaptation
 *
 */
@Service("groupApi")
public class AbGroupApiImpl implements GroupApi {

	private final RoleManager roleManager;
	private final GroupManager groupManager;
	private final OrgRelationManager orgRelationManager;
	private final OrgPostManager orgPostManager;
	private final OrgRelationMapper orgRelationMapper;

	public AbGroupApiImpl(RoleManager roleManager, GroupManager groupManager, OrgRelationManager orgRelationManager, OrgPostManager orgPostManager, OrgRelationMapper orgRelationMapper) {
		this.roleManager = roleManager;
		this.groupManager = groupManager;
		this.orgRelationManager = orgRelationManager;
		this.orgPostManager = orgPostManager;
		this.orgRelationMapper = orgRelationMapper;
	}

	@Override
	public Iterator<? extends IGroup> getByGroupIds(String groupType, Collection<String> groupIds) {
		Iterator<? extends IGroup> groupIterator = Collections.emptyIterator();
		//Role
		if (StrUtil.equalsIgnoreCase(GroupType.ROLE.getType(), groupType)) {
			groupIterator = roleManager.selectByIds(groupIds).stream().map(AbRole::newRole).iterator();
		}
		//post
		else if (StrUtil.equalsIgnoreCase(GroupType.POST.getType(), groupType)) {
			groupIterator = orgPostManager.getByIds(groupIds).stream().map(AbRole::newPost).iterator();
		}
		//organization
		else if (StrUtil.equalsIgnoreCase(GroupType.ORG.getType(), groupType)) {
			groupIterator = groupManager.selectByIds(groupIds).stream().map(AbOrg::new).iterator();
		}
		return groupIterator;
	}

	@Override
	public List<IGroup> getByGroupTypeAndUserId(String groupType, String userId) {
		if (StrUtil.equalsIgnoreCase(GroupType.ROLE.getType(), groupType)) {
			return CollUtil.map(roleManager.getByUserId(userId), AbRole::newRole, true);
		} else if (StrUtil.equalsIgnoreCase(GroupType.POST.getType(), groupType)) {
			return CollUtil.map(orgRelationManager.getUserRelation(userId, RelationTypeConstant.POST_USER.getKey()), AbRole::newPost, true);
		} else if (StrUtil.equalsIgnoreCase(GroupType.ORG.getType(), groupType)) {
			return CollUtil.map(groupManager.getByUserId(userId), AbOrg::new, false);
		}
		return new ArrayList<>();
	}

	@Override
	public Iterator<? extends IGroup> getByGroupCodes(String groupType, Collection<String> groupCodes) {
		Iterator<? extends IGroup> groupIterator = Collections.emptyIterator();
		if (StrUtil.equalsIgnoreCase(GroupType.ROLE.getType(), groupType)) {
			groupIterator = roleManager.selectByCodes(groupCodes).stream().map(AbRole::newRole).iterator();
		} else if (StrUtil.equalsIgnoreCase(GroupType.POST.getType(), groupType)) {
			groupIterator = orgPostManager.getByIds(groupCodes).stream().map(AbRole::newPost).iterator();
		} else if (StrUtil.equalsIgnoreCase(GroupType.ORG.getType(), groupType)) {
			groupIterator = groupManager.selectByCodes(groupCodes).stream().map(AbOrg::new).iterator();
		}
		return groupIterator;
	}

	@Override
	public List<? extends IGroup> queryFilter(String groupType, QueryParamDTO queryParamDTO) {
		if (CharSequenceUtil.equalsIgnoreCase(GroupType.ROLE.getType(), groupType)) {
			QueryParamDTO newQueryParamDTO = convertGroupQueryParam(
					queryParamDTO,
					ImmutableMap.of("groupName", "name", "groupCode", "code", "groupId", "id", "groupLevel", "level", "enabled", new Tuple("enabled", "N"))
			);
			PageListDTO<Role> pageListDTO = roleManager.query(new DefaultAbQueryFilter(newQueryParamDTO));
			List<IGroup> roleRows = ObjectUtil.defaultIfNull(pageListDTO.getRows(), Collections.<Role>emptyList()).stream().map(AbRole::newRole).collect(Collectors.toList());
			return new PageListDTO<>(pageListDTO.getPageSize(), pageListDTO.getPage(), pageListDTO.getTotal(), roleRows);
		} else if (CharSequenceUtil.equalsIgnoreCase(GroupType.POST.getType(), groupType)) {
			DefaultAbQueryFilter queryFilter = convertPostQueryParam(queryParamDTO);
			PageListDTO<OrgPostVO> roles = orgRelationMapper.queryPosts(queryFilter);
			return new PageListDTO<>(roles.getPageSize(), roles.getPage(), roles.getTotal(), CollUtil.map(roles.getRows(), AbOrgRelation::newPost, true));
		} else if (CharSequenceUtil.equalsIgnoreCase(GroupType.ORG.getType(), groupType)) {
			Map.Entry<String, Object> userCountEntry = Optional.ofNullable(queryParamDTO.getQueryParam()).map(Map::entrySet).orElseGet(Collections::emptySet).stream().filter(entry -> StrUtil.startWith(entry.getKey(), "userCount")).findFirst().orElse(null);
			QueryParamDTO newQueryParamDTO = convertGroupQueryParam(queryParamDTO, ImmutableMap.of("groupId", "id", "groupName", "name"));
			// Organization tree including number of users
			if (Objects.nonNull(userCountEntry)) {
				newQueryParamDTO.setQueryParam(MapUtil.filter(queryParamDTO.getQueryParam(), mapEntry -> !mapEntry.getKey().equals(userCountEntry.getKey())));
				return ObjectUtil.defaultIfNull(groupManager.queryGroup(newQueryParamDTO), Collections.<GroupVO>emptyList()).stream().map(AbOrg::of).collect(Collectors.toList());
			} else {
				PageListDTO<Group> pageListDTO = groupManager.query(new DefaultAbQueryFilter(newQueryParamDTO));
				return ObjectUtil.defaultIfNull(pageListDTO.getRows(), Collections.<Group>emptyList()).stream().map(AbOrg::new).collect(Collectors.toList());
			}
		}
		throw new IllegalArgumentException("Undefined group type, " + groupType);
	}

	private QueryParamDTO convertGroupQueryParam(QueryParamDTO originQueryParamDTO, Map<String, Object> fieldMapping) {
		if (CollUtil.isEmpty(originQueryParamDTO.getQueryParam()) && CharSequenceUtil.isEmpty(originQueryParamDTO.getSortColumn())) {
			return originQueryParamDTO;
		}

		Map<String, Object> newParamMap = MapUtil.newHashMap(CollUtil.size(originQueryParamDTO.getQueryParam()));

		// Rewrite query fields
		CollUtil.forEach(originQueryParamDTO.getQueryParam(), (CollUtil.KVConsumer<String, Object>) (key, value, index) -> {
			int dollarIndex = key.lastIndexOf(StrPool.DOLLAR);
			String newKey = key;
			if (dollarIndex != CharSequenceUtil.INDEX_NOT_FOUND) {
				Object mapField = fieldMapping.get(key.substring(0, dollarIndex));
				if (mapField instanceof String) {
					newKey = mapField + key.substring(dollarIndex);
				} else if (mapField instanceof Tuple) {
					// Mapping Data Field Types
					Tuple mapFieldTuple = (Tuple) mapField;
					newKey = mapFieldTuple.get(0) + "$" + mapFieldTuple.get(1) + key.substring(dollarIndex + 2);
				}
			}
			newParamMap.put(newKey, value);
		});

		QueryParamDTO newQueryParamDTO = originQueryParamDTO.clone();
		newQueryParamDTO.setQueryParam(newParamMap);

		// Override sort fields
		if (CharSequenceUtil.isNotEmpty(newQueryParamDTO.getSortColumn())) {
			Object mapField = fieldMapping.get(newQueryParamDTO.getSortColumn());
			if (mapField instanceof String) {
				newQueryParamDTO.setSortColumn((String) mapField);
			} else if (mapField instanceof Tuple) {
				Tuple mapFieldTuple = (Tuple) mapField;
				newQueryParamDTO.setSortColumn(mapFieldTuple.get(0));
			}
		}
		return newQueryParamDTO;
	}

	private DefaultAbQueryFilter convertPostQueryParam(QueryParamDTO queryParamDTO) {
		// Get job title filter conditions
		Map<String, Object> newQueryParam = MapUtil.newHashMap(CollUtil.size(queryParamDTO.getQueryParam()));

		Pair<String, Map<String, String>> nameFilterPair = null;

		// Processing query conditions
		for (Map.Entry<String, Object> entry : ObjectUtil.defaultIfNull(queryParamDTO.getQueryParam(), Collections.<String, Object>emptyMap()).entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();

			int dollarIndex = key.lastIndexOf('$');
			if (dollarIndex != CharSequenceUtil.INDEX_NOT_FOUND) {
				String fieldName = key.substring(0, dollarIndex);
				// Position Title
				if (CharSequenceUtil.equalsIgnoreCase(fieldName, "name")) {
					// Check if the value is not empty
					if(ObjectUtil.isNotEmpty(value)) {
						ConditionType conditionType = ConditionType.formKey(key.substring(dollarIndex + 2));
						StringBuilder sqlBuilder = new StringBuilder("CONCAT_WS('-', ogroup.name_, orole.name_) ");
						if (ConditionType.IN.equals(conditionType) || ConditionType.NOT_IN.equals(conditionType)) {
							sqlBuilder.append(conditionType.condition()).append(" ");
							Map<String, String> fieldValues = MapUtil.newHashMap();
							sqlBuilder.append("(");
							for (ListIterator<String> listIterator = Convert.toList(String.class, value).listIterator(); listIterator.hasNext(); ) {
								String itemName = "name_" + listIterator.nextIndex();
								sqlBuilder.append("#{").append(itemName).append("}");
								fieldValues.put(itemName, listIterator.next());
								if (listIterator.hasNext()) {
									sqlBuilder.append(",");
								}
							}
							sqlBuilder.append(")");
							nameFilterPair = Pair.of(sqlBuilder.toString(), fieldValues);
						} else{
							String valueString = Convert.toStr(value);
							if (ConditionType.LIKE.equals(conditionType)) {
								sqlBuilder.append(conditionType.condition()).append(" ");
								valueString = CharSequenceUtil.addPrefixIfNot(valueString, "%");
								valueString = CharSequenceUtil.addSuffixIfNot(valueString, "%");
							}else if(ConditionType.RIGHT_LIKE.equals(conditionType)){
								sqlBuilder.append(" LIKE ");
								valueString = CharSequenceUtil.addPrefixIfNot(valueString, "%");
							} else if (ConditionType.LEFT_LIKE.equals(conditionType)) {
								sqlBuilder.append(" LIKE ");
								valueString = CharSequenceUtil.addSuffixIfNot(valueString, "%");
							}else{
								sqlBuilder.append(conditionType.condition()).append(" ");
							}
							sqlBuilder.append("#{name}");
							nameFilterPair = Pair.of(sqlBuilder.toString(), ImmutableMap.of("name", Convert.toStr(valueString)));
						}
					}
				} else if (CharSequenceUtil.equalsIgnoreCase(fieldName, "id")) {
					// Job ID Processing
					List<String> groupIds = new ArrayList<>();
					List<String> roleIds = new ArrayList<>();
					for (String id : Convert.toList(String.class, value)) {
						List<String> groupIdUnderlineRoleId = CharSequenceUtil.split(id, StrPool.UNDERLINE);
						Assert.isTrue(groupIdUnderlineRoleId.size() == 2, "不合法的岗位ID：{}", id);
						groupIds.add(groupIdUnderlineRoleId.get(0));
						roleIds.add(groupIdUnderlineRoleId.get(1));
					}
					newQueryParam.put("ogroup.id$VIN", groupIds);
					newQueryParam.put("orole.id$VIN", roleIds);
				}else if(CharSequenceUtil.equalsIgnoreCase(fieldName, "status")){
					String newKey = fieldName + "$N" + key.substring(dollarIndex + 2);
					newQueryParam.put(newKey, value);
				}else{
					newQueryParam.put(key, value);
				}
			}else{
				newQueryParam.put(key, value);
			}
		}

		QueryParamDTO newQueryParamDTO = queryParamDTO.clone();
		newQueryParamDTO.setQueryParam(newQueryParam);

		DefaultAbQueryFilter queryFilter = new DefaultAbQueryFilter(newQueryParamDTO);
		// Job title filter
		if (nameFilterPair != null) {
			queryFilter.addParam("nameFilterSql", nameFilterPair.getKey());
			nameFilterPair.getValue().forEach(queryFilter::addParam);
		}

		// Sort Field Replacement
		if (CharSequenceUtil.isNotEmpty(queryParamDTO.getSortColumn())) {
			boolean sortAsc = CharSequenceUtil.isEmpty(queryParamDTO.getSortOrder()) || QuerySort.ASC.equalsIgnoreCase(queryParamDTO.getSortOrder());
			String[] sortColumns = new String[]{"ogroup.name_", "orole.name_"};
			queryFilter.setQuerySortList(sortAsc ? QuerySort.ascs(sortColumns) : QuerySort.descs(sortColumns));
		}

		return queryFilter;
	}
}
