package retrofit2;

import static retrofit2.Utils.methodError;

import java.lang.reflect.Method;
import java.lang.reflect.Type;


/**
 * 业务方法v2
 *
 * @author zhengzhihui1.vendor
 * @date 2024/12/12
 */
abstract class ServiceMethodV2<T> extends ServiceMethod<T> {

    static <T> ServiceMethod<T> parseAnnotationsV2(RetrofitSSE retrofit, Class<?> service, Method method) {
        RequestFactory requestFactory = RequestFactory.parseAnnotations(retrofit.retrofit, service, method);

        Type returnType = method.getGenericReturnType();
        if (Utils.hasUnresolvableType(returnType)) {
            throw methodError(
                    method,
                    "Method return type must not include a type variable or wildcard: %s",
                    returnType);
        }
        if (returnType == void.class) {
            throw methodError(method, "Service methods cannot return void.");
        }

        return HttpServiceMethodV2.parseAnnotations(retrofit, method, requestFactory);
    }
}
