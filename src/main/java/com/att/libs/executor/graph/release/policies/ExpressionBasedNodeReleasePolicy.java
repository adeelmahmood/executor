package com.att.libs.executor.graph.release.policies;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.att.libs.executor.graph.Node;

/**
 * NodeExpressionBasedReleasePolicy provides release poloicy
 * based on given EL expressions (Spring EL, similar to UEL)
 * 
 * @author aq728y
 *
 */
public class ExpressionBasedNodeReleasePolicy<N extends Node> implements ReleasePolicy<N>{

	private final ExpressionParser parser = new SpelExpressionParser();
	private final Expression expression;
	
	public ExpressionBasedNodeReleasePolicy(String expression) {
		this.expression = parser.parseExpression(expression);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.att.libs.executor.graph.strategy.ReleaseStrategy#canRelease(java.lang.Object)
	 */
	public boolean canRelease(N node) {
		//set context for expression evaluation
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setRootObject(node);
		return ((Boolean) expression.getValue(context)).booleanValue();
	}

	/*
	 * (non-Javadoc)
	 * @see com.att.libs.executor.graph.strategy.ReleaseStrategy#isRepeatable()
	 */
	public boolean isRepeatable() {
		return true;
	}
};