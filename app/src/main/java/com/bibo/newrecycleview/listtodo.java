package com.bibo.newrecycleview;



public class listtodo {
    private String studentworks;
    private String taskname;
    private String iid;

    public String getStudentworks() {
        return studentworks;
    }

    public void setStudentworks(String studentworks) {
        this.studentworks = studentworks;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getId() {
        return iid;
    }

    public void setId(String id) {
        this.iid = id;
    }

    // Constructor accepting both studentworks, taskname, and id
    public listtodo(String studentworks, String taskname, String iid) {
        this.studentworks = studentworks;
        this.taskname = taskname;
        this.iid = iid;
    }
}
