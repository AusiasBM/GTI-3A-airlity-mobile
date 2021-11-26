package com.example.tricoenvironment.airlity;

import java.util.HashMap;
import java.util.Map;
public class Root {
    private Object error;
    private Data data;
    private DatosUsuario datosUsuario;

    public Root() {
    }

    public Object getError() {
        return error;
    }
    public void setError(Object error) {
        this.error = error;
    }
    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }
    public DatosUsuario getDatosUsuario() {
        return datosUsuario;
    }
    public void setDatosUsuario(DatosUsuario datosUsuario) {
        this.datosUsuario = datosUsuario;
    }
}