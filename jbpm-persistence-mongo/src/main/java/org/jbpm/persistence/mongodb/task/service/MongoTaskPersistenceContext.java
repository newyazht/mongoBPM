package org.jbpm.persistence.mongodb.task.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import org.drools.core.util.StringUtils;
import org.jbpm.persistence.mongodb.MongoProcessStore;
import org.jbpm.persistence.mongodb.instance.MongoProcessInstanceInfo;
import org.jbpm.persistence.mongodb.task.model.MongoTaskImpl;
import org.jbpm.services.task.impl.model.AttachmentImpl;
import org.jbpm.services.task.impl.model.CommentImpl;
import org.jbpm.services.task.impl.model.ContentImpl;
import org.jbpm.services.task.impl.model.DeadlineImpl;
import org.jbpm.services.task.impl.model.GroupImpl;
import org.jbpm.services.task.impl.model.OrganizationalEntityImpl;
import org.jbpm.services.task.impl.model.TaskImpl;
import org.jbpm.services.task.impl.model.UserImpl;
import org.kie.api.runtime.Environment;
import org.kie.api.task.model.Attachment;
import org.kie.api.task.model.Comment;
import org.kie.api.task.model.Content;
import org.kie.api.task.model.Group;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.User;
import org.kie.internal.task.api.TaskPersistenceContext;
import org.kie.internal.task.api.model.Deadline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoTaskPersistenceContext implements TaskPersistenceContext {

	private static Logger logger = LoggerFactory.getLogger(MongoTaskPersistenceContext.class);
	
	private Environment env;
	private MongoProcessStore store;
	
	public final static String FIRST_RESULT = "firstResult";
    public final static String MAX_RESULTS = "maxResults";
    public final static String FLUSH_MODE = "flushMode";
    
	public MongoTaskPersistenceContext(Environment env) {
		super();
		this.env = env;
		this.store = (MongoProcessStore)env.get(MongoProcessStore.envKey);
	}

	public Task findTask(Long taskId) {
		check();
		MongoProcessInstanceInfo procInstInfo = store.findProcessInstanceByTaskId(taskId);
		for (MongoTaskImpl task:procInstInfo.getTasks()) {
			if (task.getId() == taskId) return task;
		}
		return null;
	}

	@Override
	public Task persistTask(Task task) {
		check();
		MongoProcessInstanceInfo procInstInfo = store.findProcessInstanceByTaskId(task.getId());
		for (Iterator<MongoTaskImpl> itr = procInstInfo.getTasks().iterator(); itr.hasNext();) {
			if (itr.next().getId() == task.getId()) {
				itr.remove();
			}
		}
		procInstInfo.getTasks().add(new MongoTaskImpl(task));
        return task;
	}

	@Override
	public Task updateTask(Task task) {
		check();
		return this.em.merge(task);
	}

	@Override
	public Task removeTask(Task task) {
		check();
		em.remove( task );
		
		return task;
	}

	@Override
	public Group findGroup(String groupId) {
		check();
		if( this.pessimisticLocking ) { 
            return this.em.find( GroupImpl.class, groupId, LockModeType.PESSIMISTIC_FORCE_INCREMENT );
        }
        return this.em.find( GroupImpl.class, groupId );
	}

	@Override
	public Group persistGroup(Group group) {
		check();
		try {
			this.em.persist( group );
	        if( this.pessimisticLocking ) { 
	            return this.em.find(GroupImpl.class, group.getId(), LockModeType.PESSIMISTIC_FORCE_INCREMENT );
	        }
		} catch (EntityExistsException e) {
    		throw new RuntimeException("Group already exists with " + group 
    				+ " id, please check that there is no group and user with same id");
    	}
        return group;
	}

	@Override
	public Group updateGroup(Group group) {
		check();
		return this.em.merge(group);
	}

	@Override
	public Group removeGroup(Group group) {
		check();
		em.remove( group );
		return group;
	}

	@Override
	public User findUser(String userId) {
		check();
		if( this.pessimisticLocking ) { 
            return this.em.find( UserImpl.class, userId, LockModeType.PESSIMISTIC_FORCE_INCREMENT );
        }
        return this.em.find( UserImpl.class, userId );
	}

	@Override
	public User persistUser(User user) {
		check();
		try {
			this.em.persist( user );
	        if( this.pessimisticLocking ) { 
	            return this.em.find(UserImpl.class, user.getId(), LockModeType.PESSIMISTIC_FORCE_INCREMENT );
	        }
		} catch (EntityExistsException e) {
    		throw new RuntimeException("User already exists with " + user 
    				+ " id, please check that there is no group and user with same id");
    	}
        return user;
	}

	@Override
	public User updateUser(User user) {
		check();
		return this.em.merge(user);
	}

	@Override
	public User removeUser(User user) {
		check();
		em.remove( user );
		return user;
	}

	@Override
	public OrganizationalEntity findOrgEntity(String orgEntityId) {
		check();
		if( this.pessimisticLocking ) { 
            return this.em.find( OrganizationalEntityImpl.class, orgEntityId, LockModeType.PESSIMISTIC_FORCE_INCREMENT );
        }
        return this.em.find( OrganizationalEntityImpl.class, orgEntityId );
	}

	@Override
	public OrganizationalEntity persistOrgEntity(OrganizationalEntity orgEntity) {
		check();
	    	
        if (!StringUtils.isEmpty(orgEntity.getId())) {
        	try {
	        	this.em.persist( orgEntity );
	            if( this.pessimisticLocking ) { 
	                return this.em.find(OrganizationalEntityImpl.class, orgEntity.getId(), LockModeType.PESSIMISTIC_FORCE_INCREMENT );
	            }
        	} catch (EntityExistsException e) {
        		throw new RuntimeException("Organizational entity already exists with " + orgEntity 
        				+ " id, please check that there is no group and user with same id");
        	}
        } 
		
        return orgEntity;
	}

	@Override
	public OrganizationalEntity updateOrgEntity(OrganizationalEntity orgEntity) {
		check();
		return this.em.merge(orgEntity);
	}

	@Override
	public OrganizationalEntity removeOrgEntity(OrganizationalEntity orgEntity) {
		check();
		em.remove( orgEntity );
		return orgEntity;
	}

	@Override
	public Content findContent(Long contentId) {
		check();
		if( this.pessimisticLocking ) { 
            return this.em.find( ContentImpl.class, contentId, LockModeType.PESSIMISTIC_FORCE_INCREMENT );
        }
        return this.em.find( ContentImpl.class, contentId );
	}

	@Override
	public Content persistContent(Content content) {
		check();
		this.em.persist( content );
        if( this.pessimisticLocking ) { 
            return this.em.find(ContentImpl.class, content.getId(), LockModeType.PESSIMISTIC_FORCE_INCREMENT );
        }
        return content;
	}

	@Override
	public Content updateContent(Content content) {
		check();
		return this.em.merge(content);
	}

	@Override
	public Content removeContent(Content content) {
		check();
		em.remove( content );
		return content;
	}

	@Override
	public Attachment findAttachment(Long attachmentId) {
		check();
		if( this.pessimisticLocking ) { 
            return this.em.find( AttachmentImpl.class, attachmentId, LockModeType.PESSIMISTIC_FORCE_INCREMENT );
        }
        return this.em.find( AttachmentImpl.class, attachmentId );
	}

	@Override
	public Attachment persistAttachment(Attachment attachment) {
		check();
		this.em.persist( attachment );
        if( this.pessimisticLocking ) { 
            return this.em.find(AttachmentImpl.class, attachment.getId(), LockModeType.PESSIMISTIC_FORCE_INCREMENT );
        }
        return attachment;
	}

	@Override
	public Attachment updateAttachment(Attachment attachment) {
		check();
		return this.em.merge(attachment);
	}

	@Override
	public Attachment removeAttachment(Attachment attachment) {
		check();
		em.remove( attachment );
		return attachment;
	}

	@Override
	public Comment findComment(Long commentId) {
		check();
		if( this.pessimisticLocking ) { 
            return this.em.find( CommentImpl.class, commentId, LockModeType.PESSIMISTIC_FORCE_INCREMENT );
        }
        return this.em.find( CommentImpl.class, commentId );
	}

	@Override
	public Comment persistComment(Comment comment) {
		check();
		this.em.persist( comment );
        if( this.pessimisticLocking ) { 
            return this.em.find(CommentImpl.class, comment.getId(), LockModeType.PESSIMISTIC_FORCE_INCREMENT );
        }
        return comment;
	}

	@Override
	public Comment updateComment(Comment comment) {
		check();
		return this.em.merge(comment);
	}

	@Override
	public Comment removeComment(Comment comment) {
		check();
		em.remove( comment );
		return comment;
	}

	@Override
	public Deadline findDeadline(Long deadlineId) {
		check();
		if( this.pessimisticLocking ) { 
            return this.em.find( DeadlineImpl.class, deadlineId, LockModeType.PESSIMISTIC_FORCE_INCREMENT );
        }
        return this.em.find( DeadlineImpl.class, deadlineId );
	}

	@Override
	public Deadline persistDeadline(Deadline deadline) {
		check();
		this.em.persist( deadline );
        if( this.pessimisticLocking ) { 
            return this.em.find(DeadlineImpl.class, deadline.getId(), LockModeType.PESSIMISTIC_FORCE_INCREMENT );
        }
        return deadline;
	}

	@Override
	public Deadline updateDeadline(Deadline deadline) {	
		check();
		return this.em.merge(deadline);
	}

	@Override
	public Deadline removeDeadline(Deadline deadline) {
		check();
		em.remove( deadline );
		return deadline;
	}

	@Override
	public <T> T queryWithParametersInTransaction(String queryName,
			Map<String, Object> params, Class<T> clazz) {
		check();
		Query query = this.em.createNamedQuery(queryName);
		return queryStringWithParameters(params, false, LockModeType.NONE, clazz, query);
	}

	@Override
	public <T> T queryAndLockWithParametersInTransaction(String queryName,
			Map<String, Object> params, boolean singleResult, Class<T> clazz) {
		check();
		Query query = this.em.createNamedQuery(queryName);
		return queryStringWithParameters(params, singleResult, LockModeType.NONE, clazz, query);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T queryInTransaction(String queryName, Class<T> clazz) {
		check();
		Query query = this.em.createNamedQuery(queryName);
		return (T) query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T queryStringInTransaction(String queryString, Class<T> clazz) {
		check();
		Query query = this.em.createQuery(queryString);
		return (T) query.getResultList();
	}

	@Override
	public <T> T queryStringWithParametersInTransaction(String queryString,
			Map<String, Object> params, Class<T> clazz) {
		check();
		Query query = this.em.createQuery(queryString);
				
		return queryStringWithParameters(params, false, LockModeType.NONE, clazz, query);
	}

	
	@Override	
	public <T> T queryAndLockStringWithParametersInTransaction(
			String queryName, Map<String, Object> params, boolean singleResult,
			Class<T> clazz) {
		check();
		Query query = this.em.createNamedQuery(queryName);
		return queryStringWithParameters(params, singleResult, LockModeType.PESSIMISTIC_FORCE_INCREMENT, clazz, query);	
	}

	@Override
	public int executeUpdateString(String updateString) {
		check();
		Query query = this.em.createQuery(updateString);
		return query.executeUpdate();
	}

	@Override
	public HashMap<String, Object> addParametersToMap(Object... parameterValues) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
        
        if( parameterValues.length % 2 != 0 ) { 
            throw new RuntimeException("Expected an even number of parameters, not " + parameterValues.length);
        }
        
        for( int i = 0; i < parameterValues.length; ++i ) {
            String parameterName = null;
            if( parameterValues[i] instanceof String ) { 
                parameterName = (String) parameterValues[i];
            } else { 
                throw new RuntimeException("Expected a String as the parameter name, not a " + parameterValues[i].getClass().getSimpleName());
            }
            ++i;
            parameters.put(parameterName, parameterValues[i]);
        }
        
        return parameters;
	}

	@Override
	public <T> T persist(T object) {
		check();
		this.em.persist( object );        
        return object;
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		check();
		if( this.pessimisticLocking ) { 
            return this.em.find( entityClass, primaryKey, LockModeType.PESSIMISTIC_FORCE_INCREMENT );
        }
        return this.em.find( entityClass, primaryKey );
	}

	@Override
	public <T> T remove(T entity) {
		check();
		em.remove( entity );
		return entity;
	}

	@Override
	public <T> T merge(T entity) {
		check();
		return this.em.merge(entity);
	}

	@SuppressWarnings("unchecked")
	private <T> T queryStringWithParameters(Map<String, Object> params, boolean singleResult, LockModeType lockMode,
			Class<T> clazz, Query query) {
		;
		if (lockMode != null) {
			query.setLockMode(lockMode);
		}
		if (params != null && !params.isEmpty()) {
			for (String name : params.keySet()) {
				if (FIRST_RESULT.equals(name)) {
					query.setFirstResult((Integer) params.get(name));
					continue;
				}
				if (MAX_RESULTS.equals(name)) {
					query.setMaxResults((Integer) params.get(name));
					continue;
				}
				if (FLUSH_MODE.equals(name)) {
					query.setFlushMode(FlushModeType.valueOf((String) params.get(name)));
					continue;
				}
				query.setParameter(name, params.get(name));
			}
		}
		if (singleResult) {
			return (T) query.getSingleResult();
		}
		return (T) query.getResultList();
	}

	@Override
	public boolean isOpen() {
		return store != null;
	}

	@Override
	public void joinTransaction() {
	}

	@Override
	public void close() {
		check();
	}
	
	protected void check() {
		if (store == null) {
			throw new IllegalStateException("MongoProcessStore is not initialized, exiting...");
		}
	}

}
