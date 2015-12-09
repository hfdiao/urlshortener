/**
 * 
 */
package github.hfdiao.urlshortener.advice;

import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import github.hfdiao.urlshortener.exception.ResourceNotFoundException;

/**
 * @author dhf
 *
 */
@Aspect
@Component
public class ServiceLogAdvice {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceLogAdvice.class);

	private static final String POINTCUT = "@within(org.springframework.stereotype.Service)";

	private static final Set<Class<?>> EXCEPTIONS_IGNORED = new HashSet<>();

	static {
		EXCEPTIONS_IGNORED.add(IllegalArgumentException.class);
		EXCEPTIONS_IGNORED.add(ResourceNotFoundException.class);
	}

	@Pointcut(POINTCUT)
	public void pointcut() {
	}

	@AfterReturning(pointcut = "pointcut()", returning = "returning")
	public void afterReturning(JoinPoint joinPoint, Object returning) throws Throwable {
		String cmd = joinPoint.getSignature().toShortString();
		String args = asString(joinPoint.getArgs());

		LOG.info("cmd: {}, args: {}, returning: {}", new Object[] { cmd, args, returning });
	}

	@AfterThrowing(pointcut = "pointcut()", throwing = "ex")
	public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
		if (ignoreException(ex)) {
			return;
		}

		String cmd = joinPoint.getSignature().toShortString();
		String args = asString(joinPoint.getArgs());
		String exception = ex.getClass().getSimpleName();

		LOG.error("cmd: {}, args: {}, exception: {}", new Object[] { cmd, args, exception, ex });
	}

	private static boolean ignoreException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		for (Class<?> clazz : EXCEPTIONS_IGNORED) {
			if (clazz.isInstance(ex)) {
				return true;
			}
		}
		return false;
	}

	private static String asString(Object[] objects) {
		if (objects == null) {
			return "null";
		}

		StringBuilder stringBuilder = new StringBuilder(128);
		stringBuilder.append("[");
		for (int i = 0; i < objects.length; ++i) {
			if (i != 0) {
				stringBuilder.append(", ");
			}

			Object obj = objects[i];
			if (obj == null) {
				stringBuilder.append("null");
			} else {
				stringBuilder.append(obj.toString());
			}
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}
