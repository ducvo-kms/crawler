package asia.ducvo.crawler;

import org.springframework.http.client.ClientHttpRequestInterceptor;

public interface Authentication<T> {
  String getToken(T properties);

  ClientHttpRequestInterceptor interceptor();
}
