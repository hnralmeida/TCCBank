//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package bank.BancoCentral.service;

import bank.BancoCentral.exception.RegistroNotFoundException;
import bank.BancoCentral.exception.RegistroNotUpdated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class _GenericService<TipoEntidade, RepositorioGenerics extends JpaRepository<TipoEntidade, UUID>> implements _GenericServiceTypes<TipoEntidade> {
    protected RepositorioGenerics repositoryGenerics;

    protected _GenericService(RepositorioGenerics repositoryGenerics) {
        this.repositoryGenerics = repositoryGenerics;
    }

    protected _GenericService() {
    }

    public List<TipoEntidade> listar() {
        try {
            return this.repositoryGenerics.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao listar entidades", e);
        }
    }

    public TipoEntidade buscarPorId(UUID id) {
        try {
            Optional<TipoEntidade> optionalEntity = this.repositoryGenerics.findById(id);
            return (TipoEntidade)optionalEntity.orElseThrow(() -> new RegistroNotFoundException(id));
        } catch (RegistroNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar entidade por ID", e);
        }
    }

    public TipoEntidade criar(TipoEntidade entity) {
        try {
            return (TipoEntidade)this.repositoryGenerics.save(entity);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar entidade", e);
        }
    }

    public void excluir(UUID id) {
        try {
            Optional<TipoEntidade> optionalEntity = this.repositoryGenerics.findById(id);
            TipoEntidade entity = (TipoEntidade)optionalEntity.orElseThrow(() -> new RegistroNotFoundException(id));
            this.repositoryGenerics.delete(entity);
        } catch (RegistroNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao excluir entidade", e);
        }
    }

    public TipoEntidade atualizar(UUID id, TipoEntidade entityAtualizada) {
        try {
            Optional<TipoEntidade> optionalEntity = this.repositoryGenerics.findById(id);
            optionalEntity.orElseThrow(() -> new RegistroNotUpdated(id));
            return (TipoEntidade)this.repositoryGenerics.save(entityAtualizada);
        } catch (RegistroNotUpdated e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar entidade", e);
        }
    }
}
