package unti;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import poly.cafe.entity.User;

/**
 * Lớp tiện ích hỗ trợ truy vấn và chuyển đổi sang đối tượng
 *
 * @author NghiemN
 * @version 1.0
 */
public class XQuery {

    /**
     * Truy vấn 1 đối tượng
     *
     * @param <B> kiểu của đối tượng cần chuyển đổi
     * @param beanClass lớp của đối tượng kết quả
     * @param sql câu lệnh truy vấn
     * @param values các giá trị cung cấp cho các tham số của SQL
     * @return kết quả truy vấn
     * @throws RuntimeException lỗi truy vấn
     */
    public static <B> B getSingleBean(Class<B> beanClass, String sql, Object... values) {
        List<B> list = XQuery.getBeanList(beanClass, sql, values);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Truy vấn nhiều đối tượng
     *
     * @param <B> kiểu của đối tượng cần chuyển đổi
     * @param beanClass lớp của đối tượng kết quả
     * @param sql câu lệnh truy vấn
     * @param values các giá trị cung cấp cho các tham số của SQL
     * @return kết quả truy vấn
     * @throws RuntimeException lỗi truy vấn
     */
    public static <B> List<B> getBeanList(Class<B> beanClass, String sql, Object... values) {
        List<B> list = new ArrayList<>();
        try {
            ResultSet resultSet = XJdbc.executeQuery(sql, values);
            while (resultSet.next()) {
                list.add(XQuery.readBean(resultSet, beanClass));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    /**
     * Tạo bean với dữ liệu đọc từ bản ghi hiện tại
     *
     * @param <B> kiểu của đối tượng cần chuyển đổi
     * @param resultSet tập bản ghi cung cấp dữ liệu
     * @param beanClass lớp của đối tượng kết quả
     * @return kết quả truy vấn
     * @throws RuntimeException lỗi truy vấn
     */
    private static <B> B readBean(ResultSet resultSet, Class<B> beanClass) throws Exception {
    B bean = beanClass.getDeclaredConstructor().newInstance();
    Method[] methods = beanClass.getDeclaredMethods();
    for (Method method : methods) {
        String name = method.getName();
        if (name.startsWith("set") && method.getParameterCount() == 1) {
            try {
                String fieldName = name.substring(3); // Tên trường, VD: Gia
                Object value;

                // Nếu trường là "Gia" thì lấy kiểu BigDecimal
                if(fieldName.equalsIgnoreCase("Gia")) {
                    value = resultSet.getBigDecimal(fieldName);
                } else {
                    value = resultSet.getObject(fieldName);
                }

                // Chuyển đổi kiểu nếu là BigDecimal
                if (value instanceof BigDecimal) {
                    Class<?> paramType = method.getParameterTypes()[0];
                    if (paramType == Double.class || paramType == double.class) {
                        value = ((BigDecimal) value).doubleValue();
                    } else if (paramType == Float.class || paramType == float.class) {
                        value = ((BigDecimal) value).floatValue();
                    } else if (paramType == Integer.class || paramType == int.class) {
                        value = ((BigDecimal) value).intValue();
                    }
                    // Nếu paramType là BigDecimal thì giữ nguyên
                }

                method.invoke(bean, value);
            } catch (Exception e) {
                System.out.printf("Error setting field '%s': %s\n", name.substring(3), e.getMessage());
            }
        }
    }
    return bean;
}

    public static void main(String[] args) {
//        demo1();
//        demo2();
    }

//    private static void demo1() {
//        String sql = "SELECT * FROM Users WHERE Username=? AND Password=?";
//        User user = XQuery.getSingleBean(User.class, sql, "NghiemN", "123456");
//    }
//
//    private static void demo2() {
//        String sql = "SELECT * FROM Users WHERE Fullname LIKE ?";
//        List<User> list = XQuery.getBeanList(User.class, sql, "%Nguyễn %");
//    }
}