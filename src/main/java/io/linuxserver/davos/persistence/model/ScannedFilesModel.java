package io.linuxserver.davos.persistence.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class ScannedFilesModel {

    @Id
    @GeneratedValue
    public Long id;
    
    @OneToMany(orphanRemoval = true, mappedBy = "scannedFiles", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<ScannedFileModel> scannedFiles = new ArrayList<ScannedFileModel>();
}
