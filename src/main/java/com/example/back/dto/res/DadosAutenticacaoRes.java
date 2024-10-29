package com.example.back.dto.res;

import com.example.back.entity.LoginInfo;

public record DadosAutenticacaoRes (String token, LoginInfo loginInfo) {
}
