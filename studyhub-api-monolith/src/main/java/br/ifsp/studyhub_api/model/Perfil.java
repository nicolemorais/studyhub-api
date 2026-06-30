package br.ifsp.studyhub_api.model;

public enum Perfil {
    PROFESSOR("ROLE_PROFESSOR"),
    ALUNO("ROLE_ALUNO");

    private final String authority;

    Perfil(String authority){
        this.authority = authority;
    }

    public String getAuthority(){
        return authority;
    }
}
