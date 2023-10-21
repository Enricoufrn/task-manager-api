package com.example.taskmanagerapi.dtos;

public abstract class AbstractDTO <T>{
    public T toEntity(){
        return null;
    }
    public AbstractDTO<T> fromEntity(T entity){
        return null;
    }
}
