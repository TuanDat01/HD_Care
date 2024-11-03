package com.doctorcare.PD_project.ultil;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class UploadUltil {
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dlokeyspj",
                "api_key", "119184151935168",
                "api_secret", "119184151935168",
                "secure", true));
    }
}
