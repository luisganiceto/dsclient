package com.anicetoluis.dsclient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anicetoluis.dsclient.entities.Client;

public interface ClienteRepository extends JpaRepository<Client, Long>{

}
