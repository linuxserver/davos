package io.linuxserver.davos;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.linuxserver.davos.Version;

public class VersionTest {

    @Test
    public void shouldSetVersionBitsFromString() {
        
        Version version = new Version("2.1.3");
        
        assertThat(version.getMajor()).isEqualTo(2);
        assertThat(version.getMinor()).isEqualTo(1);
        assertThat(version.getPatch()).isEqualTo(3);
    }
    
    @Test
    public void shouldSetVersionBits() {
        
        Version version = new Version(2, 1, 3);
        
        assertThat(version.getMajor()).isEqualTo(2);
        assertThat(version.getMinor()).isEqualTo(1);
        assertThat(version.getPatch()).isEqualTo(3);
    }
    
    @Test
    public void shouldCompareToOthers() {
        
        assertThat(new Version("0.0.2").isNewerThan(new Version("0.0.1"))).isTrue();
        assertThat(new Version("0.1.0").isNewerThan(new Version("0.0.2"))).isTrue();
        assertThat(new Version("1.0.0").isNewerThan(new Version("0.2.0"))).isTrue();
        assertThat(new Version("1.1.0").isNewerThan(new Version("1.0.0"))).isTrue();
        assertThat(new Version("1.1.1").isNewerThan(new Version("1.0.1"))).isTrue();
        
        assertThat(new Version("1.1.1").isNewerThan(new Version("1.2.1"))).isFalse();
        assertThat(new Version("0.1.1").isNewerThan(new Version("0.2.1"))).isFalse();
        assertThat(new Version("0.0.0").isNewerThan(new Version("0.0.1"))).isFalse();
        
        assertThat(new Version("2.1.2").isNewerThan(new Version("2.2.0"))).isFalse();
        assertThat(new Version("2.2.0").isNewerThan(new Version("2.2.1"))).isFalse();
    }
}
