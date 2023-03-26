# Java sample with dependency

This is a Java sample with a maven dependency

```java
// dependency: org.apache.commons:commons-lang3:3.4
import org.apache.commons.lang3.StringUtils;

public class WithDependency {
    public static void main(String[] args) {
        System.out.println(StringUtils.capitalize("fooBarFoo"));
    }
}
```
