package com.bank.msreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Microservicio de reportes del sistema bancario.
 * Consume eventos de los demas microservicios y mantiene vistas de lectura
 * para generar reportes por producto en rango de fechas y ultimos movimientos.
 */
@EnableMongoAuditing
@SpringBootApplication
public class MsReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsReportApplication.class, args);
    }
}
