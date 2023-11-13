package asia.ducvo.crawler.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Pair <K, V> {
  K key;
  V value;
}
