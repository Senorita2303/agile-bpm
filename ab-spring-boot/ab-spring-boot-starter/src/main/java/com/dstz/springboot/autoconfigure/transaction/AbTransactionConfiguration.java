package com.dstz.springboot.autoconfigure.transaction;


import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.AbstractTransactionManagementConfiguration;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.config.TransactionManagementConfigUtils;
import org.springframework.transaction.interceptor.*;

/**
 * ab transaction configuration
 *
 */
@Configuration
public class AbTransactionConfiguration extends AbstractTransactionManagementConfiguration {

    @Bean(name = "abTransactionInterceptor")
    public TransactionInterceptor abTransactionInterceptor() {
        DefaultTransactionAttribute requiredTransactionAttribute = new DefaultTransactionAttribute();
        requiredTransactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        DefaultTransactionAttribute requiredReadonlyTransactionAttribute = new DefaultTransactionAttribute();
        requiredReadonlyTransactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredReadonlyTransactionAttribute.setReadOnly(true);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.addTransactionalMethod("*", requiredTransactionAttribute);
        source.addTransactionalMethod("get*", requiredReadonlyTransactionAttribute);
        source.addTransactionalMethod("query*", requiredReadonlyTransactionAttribute);
        source.addTransactionalMethod("find*", requiredReadonlyTransactionAttribute);
        source.addTransactionalMethod("is*", requiredReadonlyTransactionAttribute);
        source.addTransactionalMethod("select*", requiredReadonlyTransactionAttribute);

        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionAttributeSources(annotationTransactionAttributeSource(), source);
        transactionInterceptor.setTransactionManager(txManager);
        return transactionInterceptor;
    }


    @Bean(name = "txAdviceAdvisor")
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.dstz..manager.*.*(..))||execution(* com.dstz.*.manager.*.*(..))");
        return new DefaultPointcutAdvisor(pointcut, abTransactionInterceptor());
    }

    @Bean(name = TransactionManagementConfigUtils.TRANSACTION_ADVISOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor() {
        BeanFactoryTransactionAttributeSourceAdvisor advisor = new BeanFactoryTransactionAttributeSourceAdvisor();
        advisor.setTransactionAttributeSource(annotationTransactionAttributeSource());
        advisor.setAdvice(abTransactionInterceptor());
        if (this.enableTx != null) {
            advisor.setOrder(this.enableTx.<Integer>getNumber("order"));
        }
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TransactionAttributeSource annotationTransactionAttributeSource() {
        return new AnnotationTransactionAttributeSource();
    }
}
