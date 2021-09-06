package karate;

import com.intuit.karate.junit5.Karate;

public class KarateRunner {
    @Karate.Test
    Karate launchAllFeatures() {
        return Karate.run().relativeTo(getClass());
    }
}
