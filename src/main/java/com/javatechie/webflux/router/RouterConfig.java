package com.javatechie.webflux.router;

import com.javatechie.webflux.dto.Customer;
import com.javatechie.webflux.handler.CustomerHandler;
import com.javatechie.webflux.handler.CustomerStreamHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Autowired
    private CustomerHandler handler;

    @Autowired
    private CustomerStreamHandler streamHandler;

    @Bean
    @RouterOperations(
            {
                    @RouterOperation(
                            path = "/router/customers",
                            produces = {
                                    MediaType.APPLICATION_JSON_VALUE
                            },
                            method = RequestMethod.GET,
                            beanClass = CustomerHandler.class,
                            beanMethod = "loadCustomers",
                            operation = @Operation(
                                    operationId = "loadCustomers",
                                    responses = {
                                           @ApiResponse(
                                                   responseCode = "200",
                                                   description = "successful operation",
                                                   content = @Content(schema = @Schema(
                                                           implementation = Customer.class
                                                   ))
                                           )
                                    }
                            )
                    ),
                    @RouterOperation(
                            path = "/router/customer/{input}",
                            produces = {
                                    MediaType.APPLICATION_JSON_VALUE
                            },
                            method = RequestMethod.GET,
                            beanClass = CustomerHandler.class,
                            beanMethod = "findCustomer",
                            operation = @Operation(
                                    operationId = "findCustomer",
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200",
                                                    description = "successful operation",
                                                    content = @Content(schema = @Schema(
                                                            implementation = Customer.class
                                                    ))
                                            ),
                                            @ApiResponse(responseCode = "404",description = "customer not found with given id")
                                    },
                                    parameters = {
                                            @Parameter(in = ParameterIn.PATH,name = "input")
                                    }

                            )

                    ),
                    @RouterOperation(
                            path = "/router/customer/save",
                            produces = {
                                    MediaType.APPLICATION_JSON_VALUE
                            },
                            method = RequestMethod.POST,
                            beanClass = CustomerHandler.class,
                            beanMethod = "saveCustomer",
                            operation = @Operation(
                                    operationId = "saveCustomer",
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200",
                                                    description = "successful operation",
                                                    content = @Content(schema = @Schema(
                                                            implementation = String.class
                                                    ))
                                            )
                                    },
                                    requestBody = @RequestBody(
                                            content = @Content(schema = @Schema(
                                                    implementation = Customer.class
                                            ))
                                    )

                            )


                    )
            }
    )
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET("/router/customers", handler::loadCustomers)
                .GET("/router/customers/stream", streamHandler::getCustomers)
                .GET("/router/customer/{input}", handler::findCustomer)
                .POST("/router/customer/save", handler::saveCustomer)
                .build();

    }
}
