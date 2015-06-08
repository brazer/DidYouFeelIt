import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import by.org.cgm.seismic.ShakeDetector.Sample;
import by.org.cgm.seismic.ShakeDetector.SampleQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Author: Anatol Salanevich
 * Date: 08.06.2015
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ShakeDetectorTest {

    @Test public void testInitialShaking() {
        SampleQueue q = new SampleQueue();
        assertFalse(q.isShaking());
        //assertThat(q.isShaking()).isFalse();
    }

    /** Tests LG Ally sample rate. */
    @Test
    public void testShakingSampleCount3() {
        SampleQueue q = new SampleQueue();
        // These times approximate the data rate of the slowest device we've
        // found, the LG Ally.
        // on the LG Ally. The queue holds 500000000 ns (0.5ms) of samples or
        // 4 samples, whichever is greater.
        // 500000000
        q.add(1000000000L, false);
        q.add(1300000000L, false);
        q.add(1600000000L, false);
        q.add(1900000000L, false);
        assertContent(q, false, false, false, false);
        assertFalse(q.isShaking());
        //assertThat(q.isShaking()).isFalse();

        // The oldest two entries will be removed.
        q.add(2200000000L, true);
        q.add(2500000000L, true);
        assertContent(q, false, false, true, true);
        assertFalse(q.isShaking());
        //assertThat(q.isShaking()).isFalse();

        // Another entry should be removed, now 3 out of 4 are true.
        q.add(2800000000L, true);
        assertContent(q, false, true, true, true);
        assertTrue(q.isShaking());
        //assertThat(q.isShaking()).isTrue();

        q.add(3100000000L, false);
        assertContent(q, true, true, true, false);
        assertTrue(q.isShaking());
        //assertThat(q.isShaking()).isTrue();

        q.add(3400000000L, false);
        assertContent(q, true, true, false, false);
        assertFalse(q.isShaking());
        //assertThat(q.isShaking()).isFalse();
    }

    private void assertContent(SampleQueue q, boolean... expected) {
        List<Sample> samples = q.asList();

        StringBuilder sb = new StringBuilder();
        for (Sample s : samples) {
            sb.append(String.format("[%b,%d] ", s.accelerating, s.timestamp));
        }

        //assertThat(samples).hasSize(expected.length);
        assertEquals(expected.length, samples.size());
        for (int i = 0; i < expected.length; i++) {
            //assertThat(samples.get(i).accelerating).isEqualTo(expected[i]);
            assertEquals(expected[i], samples.get(i).accelerating);
        }
    }

    @Test public void testClear() {
        SampleQueue q = new SampleQueue();
        q.add(1000000000L, true);
        q.add(1200000000L, true);
        q.add(1400000000L, true);
        //assertThat(q.isShaking()).isTrue();
        assertTrue(q.isShaking());
        q.clear();
        //assertThat(q.isShaking()).isFalse();
        assertFalse(q.isShaking());
    }
}
