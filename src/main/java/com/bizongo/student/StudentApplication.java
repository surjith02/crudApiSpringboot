package com.bizongo.student;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student")
class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column(name="name")
    private String name;
	@Column(name="classNumber")
    private int classNumber;
	@Column(name="rollNumber")
    private int rollNumber;

    public Student(String name, int classNumber, int rollNumber) {
        this.classNumber=   classNumber;
        this.rollNumber= rollNumber;
        this.name= name;
    }
}

@RestController
@RequestMapping("/api")
public class StudentApplication {
    @Autowired
    StudentRepo repo ;

    // get all -index,http method,GET,url /students
    @GetMapping("/students")
    public ResponseEntity<List<Student>> index() {
        return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }
    @GetMapping("/students/{id}")
    public ResponseEntity<Student> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(repo.findById(id).get(), HttpStatus.OK);
    }
    //create student -> http method post, url:/students,java method create
    //request body in json format {name,classnumber,...}
    //response body {id: ,name, classNumber}
    @PostMapping("/students")
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Student s = repo.save(new Student(student.getName(), student.getClassNumber(), student.getRollNumber()));
        return new ResponseEntity<>(s,HttpStatus.CREATED);

    }

   @PutMapping("/students/{id1}")
   public ResponseEntity<Student> update(
          @PathVariable("id1") Long id ,@RequestBody Student student){
      Optional<Student> studentData =   repo.findById(id);

      if(studentData.isPresent()){
          Student stu = studentData.get();
          stu.setClassNumber(student.getClassNumber());
          stu.setName(student.getName());
          stu.setRollNumber(student.getRollNumber());


          return  new ResponseEntity<>(repo.save(stu),HttpStatus.CREATED);
      }else{
          return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

   }

    @DeleteMapping("/students/{id1}")
    public ResponseEntity<Student> delete(
            @PathVariable("id1") Long id ,@RequestBody Student student){
        Optional<Student> studentData =   repo.findById(id);

        if(studentData.isPresent()){

            repo.deleteById(id);

            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
