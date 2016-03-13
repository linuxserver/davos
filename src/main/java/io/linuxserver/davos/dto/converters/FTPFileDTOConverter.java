package io.linuxserver.davos.dto.converters;

import io.linuxserver.davos.dto.FTPFileDTO;
import io.linuxserver.davos.transfer.ftp.FTPFile;

public class FTPFileDTOConverter implements Converter<FTPFile, FTPFileDTO> {

    @Override
    public FTPFileDTO convert(FTPFile source) {

        FTPFileDTO dto = new FTPFileDTO();

        dto.directory = source.isDirectory();
        dto.modified = source.getLastModified().toString("dd MMM yyyy HH:mm:ss");
        dto.name = source.getName();
        dto.path = source.getPath();
        dto.size = source.getSize();

        if (!source.isDirectory())
            dto.extension = dto.name.substring(dto.name.lastIndexOf('.') + 1, dto.name.length());

        return dto;
    }
}
