package com.gb.eventos;

public class Reporte {
    int idReporte;
    String reportante, modulo, clasificacion, descripcion, ingeniero;

    public Reporte(int id, String rep, String mod, String clasi, String des,String ing) {
        this.idReporte = id;
        this.reportante = rep;
        this.modulo = mod;
        this.clasificacion = clasi;
        this.descripcion = des;
        this.ingeniero = ing;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public String getReportante() {
        return reportante;
    }

    public String getDescripcion() {return descripcion;}

    public String getModulo() {
        return modulo;
    }

    public String getClasificacion() {return clasificacion;}

    public String getIngeniero() {return ingeniero;}
}
