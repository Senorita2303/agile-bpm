package com.dstz.base.api.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <pre>
 * Paginated response definition
 * </pre>
 */
public class PageListDTO<T> implements List<T> {
	/**
	 * Page size
	 */
	private long pageSize = 0;
	/**
	 * Current Page
	 */
	private long page = 1;
	/**
	 * Total number of items
	 */
	private long total = 0L;
	/**
	 * Paginated list data
	 */
	private List<T> rows = null;

	public PageListDTO() {
	}

	public PageListDTO(long pageSize, long page, long total, List<T> rows) {
		super();
		this.pageSize = pageSize;
		this.page = page;
		this.total = total;
		this.rows = rows;
		if (rows == null) {
			this.rows = Collections.emptyList();
		}
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public int size() {
		return rows.size();
	}

	public boolean isEmpty() {
		return rows.isEmpty();
	}

	public boolean contains(Object o) {
		return rows.contains(o);
	}

	public Iterator<T> iterator() {
		return rows.iterator();
	}

	public Object[] toArray() {
		return rows.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return rows.toArray(a);
	}

	public boolean add(T e) {
		return rows.add(e);
	}

	public boolean remove(Object o) {
		return rows.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return rows.containsAll(c);
	}

	public boolean addAll(Collection<? extends T> c) {
		return rows.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		return rows.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return rows.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return rows.retainAll(c);
	}

	public void clear() {
		rows.clear();
	}

	public T get(int index) {
		return rows.get(index);
	}

	public T set(int index, T element) {
		return rows.set(index, element);
	}

	public void add(int index, T element) {
		rows.add(index, element);
	}

	public T remove(int index) {
		return rows.remove(index);
	}

	public int indexOf(Object o) {
		return rows.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return rows.lastIndexOf(o);
	}

	public ListIterator<T> listIterator() {
		return rows.listIterator();
	}

	public ListIterator<T> listIterator(int index) {
		return rows.listIterator(index);
	}

	public List<T> subList(int fromIndex, int toIndex) {
		return rows.subList(fromIndex, toIndex);
	}

}
