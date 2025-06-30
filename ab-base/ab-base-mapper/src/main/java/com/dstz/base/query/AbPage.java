package com.dstz.base.query;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class AbPage extends Page implements Map<String, Object> {

    private static final long serialVersionUID = 1L;

    private Map params;

    // Paging does not check CountSQL by default
    public AbPage() {
        super();
        this.searchCount = false;
    }

    public AbPage(long current, long size) {
        super(current, size, 0);
    }

    public AbPage(long current, long size, long total) {
        super(current, size, total, true);
    }

    public AbPage(long current, long size, boolean searchCount) {
        super(current, size, 0, searchCount);
    }

    public AbPage(long current, long size, long total, boolean searchCount) {
        super(current, size, total, searchCount);
    }

    public AbPage(RowBounds rowBounds, boolean searchCount) {
        super((rowBounds.getOffset() / rowBounds.getLimit()) + 1, rowBounds.getLimit(), 0, searchCount);
    }

    @Override
    public int size() {
        return params.size();
    }

    @Override
    public boolean isEmpty() {
        return CollUtil.isEmpty(params);
    }

    @Override
    public boolean containsKey(Object key) {
        return params.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return params.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return params.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return params.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return params.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        params.putAll(m);
    }

    @Override
    public void clear() {
        params.clear();
    }

    @Override
    public Set<String> keySet() {
        return params.keySet();
    }

    @Override
    public Collection<Object> values() {
        return params.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return params.entrySet();
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

}
