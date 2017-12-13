package me.ocheng.sdk.util;


import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 欧飞xml解析工具
 * @author jwli
 * @date 2017-12-12
 */
public final class OFXMLParser {


    /**
     * 解析xml格式字符串转成一个java对象
     * @param data  xml格式字符串
     * @param t      需要转的对象
     * @param clazz  是集合的字段和对象类型
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T parseStr(String data,T t,Map<String,Class> clazz)throws Exception{
        return parse(DocumentHelper.parseText(data).getRootElement(),t,clazz);
    }
    /**
     *解析xml文档成对象
     * @param root
     * @param t
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T parse(Element root, T t, Map<String,Class> clazz)throws Exception{
        List<Element> elements = root.elements();
        for(Element element : elements){
            List<Element> node = element.elements();
            if(node!=null&&node.size()>0){
                //有子集合数据,设置值
                setListVal(t,element,clazz);
            }else if(node!=null){
                setVal(t,element);
            }
        }
        return t;
    }

    /**
     * 设置单个字段
     * @param obj
     * @param element
     * @throws Exception
     */
    private static void setVal(Object obj,Element element)throws Exception{

        Class<?> clazz = obj.getClass();

        String name = element.getName();
        Object val = element.getData();

        Class<?> type = getPrivateField(name, obj).getType();
        getSetter(clazz,name,type).invoke(obj,val);

    }


    /**
     * 拿到setter方法名
     * @param name
     * @return
     */
    private static String getSetterNmae(String name){
        return "set"+name.substring(0,1).toUpperCase()+name.substring(1);
    }

    /**
     * 拿到setter
     * @param clazz
     * @param name
     * @param type
     * @return
     * @throws Exception
     */
    private static Method getSetter(Class clazz, String name, Class<?>... type)throws Exception{

        return clazz.getMethod(getSetterNmae(name),type);
    }


    private static void setListVal(Object obj,Element  element,Map<String,Class> clazz) throws Exception {
        //集合对象名称
        String name = element.getName();
        //集合元素类型
        Class clz = clazz.get(name);
        ArrayList list = new ArrayList();
        List<Element>  elements = element.elements();
        for(Element node : elements){
            //对集合元素设置值
            Object o = clz.newInstance();
            for(Element no : node.elements()){
                setVal(o,no);
            }
            list.add(o);
        }
        //把集合放进去对象

        Class<?> type = getPrivateField(name, obj).getType();
        getSetter(obj.getClass(),name,type).invoke(obj,list);
    }

    /**
     * 拿到私有字段
     * @param name
     * @param obj
     * @return
     * @throws Exception
     */
    private static Field getPrivateField(String name,Object obj) throws Exception {
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    /**
     * 拿到私有方法
     * @param name
     * @param obj
     * @param type
     * @return
     * @throws Exception
     */
    private static Method getPrivateMethod(String name,Object obj,Class<?>... type)throws Exception{
        Method method = obj.getClass().getDeclaredMethod(name, type);
        method.setAccessible(true);
        return method;
    }

    private   static void setListNodeVal(Object obj,String name,Object val) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> type = obj.getClass().getDeclaredField(name).getType();
        obj.getClass().getMethod(name.substring(0,1).toUpperCase()+name.substring(1),type).invoke(obj,val);
    }
}
