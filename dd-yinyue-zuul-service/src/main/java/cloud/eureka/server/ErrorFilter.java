package cloud.eureka.server;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class ErrorFilter extends ZuulFilter {

	private Logger log = LoggerFactory.getLogger(ErrorFilter.class);

	private static final String ERROR_STATUS_CODE_KEY = "error.status_code";

	public static final String DEFAULT_ERR_MSG = "系统繁忙,请稍后再试";

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		return ctx.containsKey(ERROR_STATUS_CODE_KEY);
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		try {
			HttpServletRequest request = ctx.getRequest();
			int statusCode = (Integer) ctx.get(ERROR_STATUS_CODE_KEY);
			String message = (String) ctx.get("error.message");
			if (ctx.containsKey("error.exception")) {
				Throwable e = (Exception) ctx.get("error.exception");
				Throwable re = getOriginException(e);
				if (re instanceof java.net.ConnectException) {
					message = "Real Service Connection refused";
					log.warn("uri:{},error:{}", request.getRequestURI(), re.getMessage());
				} else if (re instanceof java.net.SocketTimeoutException) {
					message = "Real Service Timeout";
					log.warn("uri:{},error:{}", request.getRequestURI(), re.getMessage());
				} else if (re instanceof com.netflix.client.ClientException) {
					message = re.getMessage();
					log.warn("uri:{},error:{}", request.getRequestURI(), re.getMessage());
				} else {
					log.warn("Error during filtering", e);
				}
			}
			if (StringUtils.isBlank(message)){
				message = DEFAULT_ERR_MSG;
			}
			request.setAttribute("javax.servlet.error.status_code", statusCode);
			request.setAttribute("javax.servlet.error.message", message);
//			WebUtils.responseOutJson(ctx.getResponse(), JsonUtils.toJson(new WrapperResponse<>(statusCode, message)));
		} catch (Exception e) {
			String error = "Error during filtering[ErrorFilter]";
			log.error(error, e);
//			WebUtils.responseOutJson(ctx.getResponse(), JsonUtils.toJson(new WrapperResponse<>(500, error)));
		}
		return null;
	}

	private Throwable getOriginException(Throwable e) {
		e = e.getCause();
		while (e.getCause() != null) {
			e = e.getCause();
		}
		return e;
	}

}
