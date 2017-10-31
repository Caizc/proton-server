package com.zicongcai.persistence.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.ExampleOptions;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.SearchResult;

/**
 * 
 * 基础Dao（所有Dao类都要继承该类）
 * 
 * @author caizc
 * @version 2014-08-26 09:29:29
 * 
 * @param <T>
 *            PO持久化对象
 * @param <ID>
 *            ID类型
 */
@SuppressWarnings("unchecked")
public class BaseDao<T, ID extends Serializable> extends GenericDAOImpl<T, ID> {

	@Resource(name = "sessionFactory")
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Transactional
	@Override
	public T find(Serializable id) {
		return super.find(id);
	}

	@Transactional
	@Override
	public T[] find(Serializable... ids) {
		return super.find(ids);
	}

	@Transactional
	@Override
	public T getReference(Serializable id) {
		return super.getReference(id);
	}

	@Transactional
	@Override
	public T[] getReferences(Serializable... ids) {
		return super.getReferences(ids);
	}

	@Transactional
	@Override
	public boolean save(T entity) {
		return super.save(entity);
	}

	@Transactional
	@Override
	public boolean[] save(T... entities) {
		return super.save(entities);
	}

	@Transactional
	@Override
	public boolean remove(T entity) {
		return super.remove(entity);
	}

	@Transactional
	@Override
	public void remove(T... entities) {
		super.remove(entities);
	}

	@Transactional
	@Override
	public boolean removeById(Serializable id) {
		return super.removeById(id);
	}

	@Transactional
	@Override
	public void removeByIds(Serializable... ids) {
		super.removeByIds(ids);
	}

	@Transactional
	@Override
	public List<T> findAll() {
		return super.findAll();
	}

	@Transactional
	@Override
	public List<T> search(ISearch search) {
		return (List<T>) super.search(search);
	}

	@Transactional
	@Override
	public T searchUnique(ISearch search) {
		return super.searchUnique(search);
	}

	@Transactional
	@Override
	public int count(ISearch search) {
		return super.count(search);
	}

	@Transactional
	@Override
	public SearchResult<?> searchAndCount(ISearch search) {
		return super.searchAndCount(search);
	}

	@Transactional
	@Override
	public boolean isAttached(T entity) {
		return super.isAttached(entity);
	}

	@Transactional
	@Override
	public void refresh(T... entities) {
		super.refresh(entities);
	}

	@Transactional
	@Override
	public void flush() {
		super.flush();
	}

	@Transactional
	@Override
	public Filter getFilterFromExample(T example) {
		return super.getFilterFromExample(example);
	}

	@Transactional
	@Override
	public Filter getFilterFromExample(T example, ExampleOptions options) {
		return super.getFilterFromExample(example, options);
	}

}
