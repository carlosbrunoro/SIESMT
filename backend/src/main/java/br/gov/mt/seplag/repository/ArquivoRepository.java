package br.gov.mt.seplag.repository;


import br.gov.mt.seplag.entity.Arquivo;
import br.gov.mt.seplag.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoRepository extends BaseRepository<Arquivo, Long> {

}