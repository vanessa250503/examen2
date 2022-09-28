package com.eisp.evaluacion_crudsqlite;

import java.io.Serializable;

public class Dto  implements Serializable {

    int codigo;
    String descripcion;
    double precio;
    int idcategoria;
    String nomCate;



    public Dto() {
    }

    public Dto(int codigo, String descripcion, double precio, int idcategoria, String nomCate) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.idcategoria = idcategoria;
        this.nomCate = nomCate;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(int idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getNomCate() {
        return nomCate;
    }

    public void setNomCate(String nomCate) {
        this.nomCate = nomCate;
    }
}

