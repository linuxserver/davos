package io.linuxserver.davos.dto.converters;

import static org.assertj.core.api.Assertions.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;

import io.linuxserver.davos.dto.FTPFileDTO;
import io.linuxserver.davos.transfer.ftp.FTPFile;

public class FTPFileDTOConverterTest {

    @Test
    public void shouldConvertFile() {
        
        FTPFile file = new FTPFile("name.txt", 12345l, "/some/path", new DateTime(2016, 3, 12, 14, 34, 15).getMillis(), false);
        
        FTPFileDTO dto = new FTPFileDTOConverter().convert(file);
        
        assertThat(dto.name).isEqualTo("name.txt");
        assertThat(dto.size).isEqualTo(12345l);
        assertThat(dto.path).isEqualTo("/some/path");
        assertThat(dto.modified).isEqualTo("12 Mar 2016 14:34:15");
        assertThat(dto.extension).isEqualTo("txt");
        assertThat(dto.directory).isEqualTo(false);
    }
    
    @Test
    public void extensionShouldBeNullIfDirectory() {
        
        FTPFile file = new FTPFile("name", 12345l, "/some/path", new DateTime(2016, 3, 12, 14, 34, 15).getMillis(), true);
        FTPFileDTO dto = new FTPFileDTOConverter().convert(file);
        
        assertThat(dto.extension).isNull();
        assertThat(dto.directory).isEqualTo(true);
    }
}
