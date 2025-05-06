package com.myproject.simpleonlineshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;



@Entity
@Table(name = "t_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String fileName;
    private String fileType;

    @Lob //Blob Specifier for database, used to store large amounts of binary data in a database
    //@Basic(fetch = FetchType.LAZY) only will appear in response when  you specify explicitly
    private Blob image;

    private String downloadUrl;


    @ManyToOne
    @JoinColumn(name = "product_id") // creating a column inside Image to reference to Product
    private Product product;
}

