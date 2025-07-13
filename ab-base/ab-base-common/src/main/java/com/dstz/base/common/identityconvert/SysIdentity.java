package com.dstz.base.common.identityconvert;

import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.enums.IdentityType;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Description: Process and organization connection entity interface type: user / other group types
 *
 */
public class SysIdentity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String type;

	public SysIdentity() {
	}

	public SysIdentity(String id, String name, String type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public SysIdentity(IUser user) {
		this.id = user.getUserId();
		this.name = user.getFullName();
		this.type = IdentityType.USER.getKey();
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public int hashCode() {
		return this.id.hashCode() + this.type.hashCode();
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof SysIdentity)) {
			return false;
		}

		if (StrUtil.isEmpty(id) || StrUtil.isEmpty(name)) {
			return false;
		}

		SysIdentity identity = (SysIdentity) obj;

		if (this.type.equals(identity.getType()) && this.id.equals(identity.getId())) {
			return true;
		}

		return false;
	}

	/**
	 * Create a SysIdentity instance from a group
	 *
	 * @param group group
	 * @return SysIdentity Examples
	 */
	public static SysIdentity of(IGroup group) {
		return new SysIdentity(group.getGroupId(), group.getGroupName(), group.getGroupType());
	}

	/**
	 * Create a SysIdentity instance from the parameters
	 *
	 * @param id           id
	 * @param name         name
	 * @param identityType type
	 * @return System personnel
	 */
	public static SysIdentity of(String id, String name, IdentityType identityType) {
		return new SysIdentity(id, name, identityType.getKey());
	}

	/**
	 * <pre>
	 * Returns information for display that encapsulates type and name
	 * </pre>	
	 * @return
	 */
	@Deprecated
	public String getAssign() {
		String str = "(";
		if ("role".equals(type)) {
			str += "Role";
		} else if ("org".equals(type)) {
			str += "Organization";
		} else if ("group".equals(type)) {
			str += "Group";
		} else if ("post".equals(type)) {
			str += "Post";
		} else if ("user".equals(type)) {
			str += "User";
		}
		return str + ")" + name;
	}

	/**
	 * Generate personnel assignment name
	 *
	 * @return [Personnel type] Personnel name
	 */
	public String genAssignName() {
		String typeDesc;
		if (IdentityType.USER.getKey().equalsIgnoreCase(this.type)) {
			typeDesc = IdentityType.USER.getValue();
		} else {
			typeDesc = Arrays.stream(GroupType.values()).filter(ele -> ele.getType().equalsIgnoreCase(this.type))
					.map(GroupType::getDesc).findFirst().orElse(StrUtil.EMPTY);
		}
		return String.format("[%s]%s", typeDesc, this.name);
	}

	/**
	 * Generate personnel allocation information
	 *
	 * @return Personnel Type/Personnel Name/Personnel ID
	 */
	public String genAssignInfo() {
		return String.format("%s/%s/%s", this.type, this.name, this.id);
	}
}
