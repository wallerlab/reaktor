package cluster.logging;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.google.common.base.Stopwatch;

/**
 * Logger for all methods
 * 
 * @author suzanne
 *
 */
@Aspect
@Component
public class GenericLogger {

	static Logger log = Logger.getLogger("cluster");
	
	//@Around("execution(public * cluster.services..*.*(..))")
	@Around("execution(public * cluster..*.*(..))")
	public Object logAround(ProceedingJoinPoint joinPoint){

		log.info("method " + joinPoint.getSignature().getDeclaringTypeName()
				+ "."+ joinPoint.getSignature().getName() + " has begun with " +
				"arguments : " + Arrays.toString(joinPoint.getArgs()));
		Stopwatch stopwatch = Stopwatch.createStarted();
		Object value = null;
		try {
			value = joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		stopwatch.stop();
		long millisecs = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		log.info("method " + joinPoint.getSignature().getDeclaringTypeName()
				+ "."+ joinPoint.getSignature().getName() + " has ended with" +
				" a value of " + value + " and a time of " + millisecs + " ms");
		return value;
		
	}

	/*@AfterReturning(pointcut="execution(private boolean cluster.services" +
			".listener.FileTransporterService.waitForFiles(..))",
			returning = "result")
	public void folderExists(JoinPoint joinPoint, Object result){
		
		log.info("waitForFiles terminated for file " + joinPoint.getArgs() + " with a value of " + result);
		
	}*/

}
