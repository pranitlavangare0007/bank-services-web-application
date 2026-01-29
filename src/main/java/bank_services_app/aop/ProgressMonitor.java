package bank_services_app.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ProgressMonitor {
    private  static final Logger logger= LoggerFactory.getLogger(ProgressMonitor.class);
    @Around("execution(* bank_services_app.*(..))")
    public Object monitor(ProceedingJoinPoint jp) throws Throwable {

        long start=System.currentTimeMillis();

        Object obj=jp.proceed();
        long end = System.currentTimeMillis();

        logger.info("time taken :"+(end - start) + "ms");

        return obj;
    }
}
