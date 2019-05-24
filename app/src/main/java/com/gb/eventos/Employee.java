package com.gb.eventos;


    public class Employee {
        int id;
        String pregunta, respuesta, categoria, estado, vestado;

        public Employee(int id,String pregunta, String respuesta, String categoria, String estado, String vestado) {

            this.pregunta = pregunta;
            this.respuesta = respuesta;
            this.categoria = categoria;
            this.estado = estado;
            this.vestado = vestado;
            this.id = id;
        }

        public int getId() { return id; }

        public String getPregunta() {
            return pregunta;
        }

        public String getRespuesta() {
            return respuesta;
        }

        public String getCategoria() {
            return categoria;
        }

        public String getEstado() {
            return estado;
        }

        public String getVestado() {
            return vestado;
        }

    }

