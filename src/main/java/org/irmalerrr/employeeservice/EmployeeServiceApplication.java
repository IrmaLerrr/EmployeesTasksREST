package org.irmalerrr.employeeservice;//todo - DONE - пакеты стоит формировать по такой формуле: [домен].[компания].[проект].[слой(сервис, репозиторий, корнтроллер и тд)] + у тебя микросервис по управлению сотрудниками(employeeservice), таски это как доп сущность

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@Slf4j
@EnableJpaAuditing
public class EmployeeServiceApplication { //todo - DONE - называем Названиесервисa+Application
    public static void main(String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }

//todo - DONE - создание бинов лучше выносить в отдельный конфигурационный класс помеченый @Configuration
//todo - DONE -  Стараемся писать код чисто) если не используется что то, то сносим

}
