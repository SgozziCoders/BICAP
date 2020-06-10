package it.unimib.bicap.wrapper;

public class DataWrapper<T> {
    Exception error;
    T data;

    public DataWrapper(Exception error, T data){
        this.error = error;
        this.data = data;
    }

    public Exception getError() {
        return error;
    }

    public T getData() {
        return data;
    }
}
