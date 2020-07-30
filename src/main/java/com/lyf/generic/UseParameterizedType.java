package com.lyf.generic;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class UseParameterizedType {
    static class ParameterizedResponse implements ParameterizedType {
        private Type actualType;

        public ParameterizedResponse(Type actualType) {
            this.actualType = actualType;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{actualType};
        }

        @Override
        public Type getRawType() {
            return ResponseData.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

        public Type getTypeVar() {
            return actualType;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ParameterizedResponse)) {
                return false;
            }
            ParameterizedResponse that = (ParameterizedResponse) o;
            if (!getRawType().equals(that.getTypeVar())) {
                return false;
            }
            return getTypeVar().equals(that.getTypeVar());
        }
    }

    public static <T> T dataFromResponse(String responseJson, Class<T> clazz) {
        ResponseData responseData = new Gson().fromJson(responseJson, new ParameterizedResponse(clazz));
        System.out.println(responseData);
        if (responseData.getCode() != 200) {
            System.out.println("check code failed with error message: " + responseData.getErrorMsg());
            return null;
        }
        return (T) responseData.getData();
    }

    public static String sendRequest(String rul) {
        ResponseData<Person> data = new ResponseData<>();
        Person person = new Person();
        person.setAge(20);
        person.setName("xiaoming");
        data.setData(person);
        data.setCode(200);
        return new Gson().toJson(data);
    }

    public static void main(String[] args) {
        String data = sendRequest(null);
        Person person = dataFromResponse(data, Person.class);
        if (person != null) {
            System.out.println("name is = " + person.getName());
            System.out.println("age is = " + person.getAge());
        }
    }
}
