package com.example.demo.Util;

import com.example.demo.JwtHelper.JwtHelper;
import com.example.demo.user.User;
import com.google.gson.Gson;
import org.json.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Util {
    private static User currentUser;

    public static void setCurrentUser(String jwtToken) {
        String body = JwtHelper.decodeJwt(jwtToken);
        JSONObject jsonObject = new JSONObject(body);
        Long userId = Long.parseLong(jsonObject.get("userId").toString());
        String name = jsonObject.get("name").toString();
        String surname = jsonObject.get("surname").toString();
        Long companyId = Long.parseLong(jsonObject.get("companyId").toString());
        String companyName = jsonObject.get("companyName").toString();
        String role = jsonObject.get("role").toString();
        String authorizations = jsonObject.get("authorizations").toString();
        currentUser = new User(userId,name,surname,companyId,companyName,role, authorizations);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static ArrayList<Object> stringToArrayList(String string) {
        JSONArray jsonArray = new JSONArray(string);
        ArrayList<Object> arrayList = new ArrayList<>();
        for (Object element : jsonArray) {
            arrayList.add(element);
        }
        return arrayList;
    }

    public static HashMap<String, Object> jsonObjectToHashMap(JSONObject jsonObject) {
        HashMap<String, Object> hashMap = new Gson().fromJson(String.valueOf(jsonObject), HashMap.class);
        return hashMap;
    }

    public static JSONObject objectToJSONObject(Object object) {
        if(object instanceof JSONObject) {
            return (JSONObject) object;
        }
        String jsonInString = new Gson().toJson(object);
        JSONObject jsonObject = new JSONObject(jsonInString);
        return jsonObject;
    }

    public static ArrayList<String> objectArrToStringArr(ArrayList<Object> objArr) {
        ArrayList<String> stringList = new ArrayList<>();
        for (Object obj : objArr) {
            if(obj == null) {
                stringList.add(null);
            }else{
                stringList.add(obj.toString());
            }
        }
        return stringList;
    }

    public static Object findObjectBy(String attribute, String value, ArrayList<Object> arrayList) {
        for(Object o : arrayList) {
            JSONObject jsonObject = objectToJSONObject(o);
            if(Objects.equals(jsonObject.get(attribute).toString(), value)) {
                return o;
            }
        }
        return null;
    }

    public static String decimalStringToLongString(String decimalString) {
        long l = (long)Double.parseDouble(decimalString);
        return String.valueOf(l);
    }

    public static ArrayList<String> getIntersection(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> result = new ArrayList<>();
        for(String s: arr1) {
            if(arr2.contains(s)){
                result.add(s);
            }
        }
        return result;
    }
}
