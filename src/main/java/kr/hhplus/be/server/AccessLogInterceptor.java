package kr.hhplus.be.server;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.File;


public class AccessLogInterceptor implements HandlerInterceptor {
    private final Logger accessLogger;

    public AccessLogInterceptor(String accessLogPath) {
        new File(accessLogPath).getParentFile().mkdirs();

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level - %msg%n");
        encoder.start();

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setContext(loggerContext);
        fileAppender.setFile(accessLogPath);
        fileAppender.setEncoder(encoder);
        fileAppender.start();

        Logger logger = loggerContext.getLogger("access-log");
        ((ch.qos.logback.classic.Logger) logger).addAppender(fileAppender);
        ((ch.qos.logback.classic.Logger) logger).setAdditive(false);

        this.accessLogger = logger;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        accessLogger.info("{} {}", request.getMethod(), request.getRequestURI());
        return true;
    }

}
