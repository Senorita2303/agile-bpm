package com.dstz.base.common.tree;

import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.map.MapUtil;
import com.dstz.base.api.model.Tree;
import com.dstz.base.common.utils.CastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Using Map as a tree implementation
 *
 */
public class MapTree extends HashMap<String, Object> implements Tree<MapTree> {

	private static final long serialVersionUID = -8739914867159343333L;

	private final TreeNodeConfig treeNodeConfig;

	public MapTree(TreeNodeConfig treeNodeConfig) {
		this.treeNodeConfig = treeNodeConfig;
	}

	public MapTree(int initialCapacity, TreeNodeConfig treeNodeConfig) {
		super(initialCapacity);
		this.treeNodeConfig = treeNodeConfig;
	}

	public static MapTree buildDefaultConfigMapTree(String id, String parentId, String name, Map<String, Object> extra) {
		return buildMapTree(TreeNodeConfig.DEFAULT_CONFIG, id, parentId, name, extra);
	}

	public static MapTree buildMapTree(TreeNodeConfig treeNodeConfig, String id, String parentId, String name, Map<String, Object> extra) {
		MapTree mapTree = new MapTree(treeNodeConfig);
		mapTree.setId(id);
		mapTree.setParentId(parentId);
		mapTree.put(treeNodeConfig.getNameKey(), name);
		if (MapUtil.isNotEmpty(extra)) {
			mapTree.putAll(extra);
		}
		return mapTree;
	}

	/**
	 * Set ID
	 *
	 * @param id id
	 */
	public void setId(String id) {
		put(treeNodeConfig.getIdKey(), id);
	}

	@Override
	public String getId() {
		return MapUtil.getStr(this, treeNodeConfig.getIdKey());
	}

	/**
	 * Set parent ID
	 *
	 * @param parentId Parent ID
	 */
	public void setParentId(String parentId) {
		put(treeNodeConfig.getParentIdKey(), parentId);
	}

	@Override
	public String getParentId() {
		return MapUtil.getStr(this, treeNodeConfig.getParentIdKey());
	}

	@Override
	public List<MapTree> getChildren() {
		return CastUtils.cast(get(treeNodeConfig.getChildrenKey()));
	}

	@Override
	public void setChildren(List<MapTree> list) {
		put(treeNodeConfig.getChildrenKey(), list);
	}

	public void addChild(MapTree child) {
		List<MapTree> children = getChildren();
		if (children == null) {
			setChildren(children = new ArrayList<>());
		}
		children.add(child);
	}
}
