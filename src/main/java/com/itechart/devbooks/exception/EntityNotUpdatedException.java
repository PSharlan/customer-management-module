package com.itechart.devbooks.exception;

public class EntityNotUpdatedException extends RuntimeException {

    private Class notUpdatedClass;
    private Long entityId;

    public EntityNotUpdatedException(Class notFoundedClass, Long entityId){
        this.entityId = entityId;
        this.notUpdatedClass = notFoundedClass;
    }

    public Class getNotUpdatedClass() {
        return notUpdatedClass;
    }

    public void setNotUpdatedClass(Class notUpdatedClass) {
        this.notUpdatedClass = notUpdatedClass;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
