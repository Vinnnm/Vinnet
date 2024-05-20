package com.vinn.vinnet.dto;

import lombok.Data;

@Data
public class ImageResponse {
    private int status;
    private String message;
    private String url;
}
