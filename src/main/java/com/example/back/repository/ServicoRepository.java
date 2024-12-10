package com.example.back.repository;

import com.example.back.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByNomeContainingIgnoreCase(String nome);

    List<Servico> findByDuracaoMinutos(Integer duracao);

    List<Servico> findByPreco(BigDecimal bigDecimal);

    List<Servico> findByDeletadoFalse();


    @Query("SELECT s FROM Servico s WHERE " +
            "s.deletado = false AND " +
            "(:nome IS NULL OR UPPER(s.nome) LIKE UPPER(CONCAT('%', :nome, '%'))) AND " +
            "(:duracao IS NULL OR s.duracaoMinutos = :duracao) AND " +
            "(:preco IS NULL OR s.preco = :preco) AND " +
            "(:descricao IS NULL OR UPPER(s.descricao) LIKE UPPER(CONCAT('%', :descricao, '%')))")
    List<Servico> filtrarServicos(@Param("nome") String nome,
                                  @Param("duracao") Integer duracao,
                                  @Param("preco") BigDecimal preco,
                                  @Param("descricao") String descricao);
}
