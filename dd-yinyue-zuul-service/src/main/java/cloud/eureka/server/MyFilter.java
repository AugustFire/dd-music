package cloud.eureka.server;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class MyFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(MyFilter.class);

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
//		Object accessToken = request.getParameter("token");
//		System.out.println(accessToken);
//		if (accessToken == null) {
//			log.warn("token is empty1");
//			ctx.setSendZuulResponse(false);
//			ctx.setResponseStatusCode(401);
//			try {
//				ctx.getResponse().getWriter().write("token is empty1");
//			} catch (Exception e) {
//			}
//
//			return null;
//		}
		return null;
	}

}
