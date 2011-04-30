package cz.poptavka.sample.service;

import com.google.common.base.Preconditions;
import cz.poptavka.sample.common.ResultCriteria;
import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.exception.DomainObjectNotFoundException;
import org.hibernate.criterion.Example;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class GenericServiceImpl<Dom extends DomainObject, Dao extends GenericDao<Dom>>
        implements GenericService<Dom, Dao> {

    /**
     * dao object.
     */
    private Dao dao;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Dom getById(long id) {
        assertConfigured();
        Dom entity = dao.findById(id);
        if (entity == null) {
            throw new DomainObjectNotFoundException(id, dao.getPersistentClass());
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Dom searchById(long id) {
        assertConfigured();
        return dao.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Dom> getAll() {
        assertConfigured();
        return dao.findAll();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Dom> getAll(ResultCriteria resultCriteria) {
        assertConfigured();
        return dao.findAll(resultCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Dom create(Dom entity) {
        if (entity == null) {
            throw new NullPointerException("The given entity must not be null!");
        }
        assertConfigured();
        dao.create(entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Dom update(Dom entity) {
        if (entity == null) {
            throw new NullPointerException("The given entity must not be null!");
        }
        assertConfigured();
        dao.update(entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void remove(Dom entity) {
        if (entity == null) {
            throw new NullPointerException("The given entity must not be null!");
        }
        assertConfigured();
        dao.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void removeById(long id) {
        Dom object = searchById(id);
        if (object != null) {
            remove(object);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Dom refresh(Dom entity) {
        if (entity == null) {
            throw new NullPointerException("The given entity must not be null!");
        }
        assertConfigured();
        return dao.refresh(entity);
    }


    /** {@inheritDoc} */
    @Override
    public List<Dom> findByExample(Dom example) {
        return this.dao.findByExample(example);
    }

    /** {@inheritDoc} */
    @Override
    public List<Dom> findByExample(Dom example, ResultCriteria resultCriteria) {
        return this.dao.findByExample(example, resultCriteria);
    }

    @Override
    public List<Dom> findByExampleCustom(Example customExample) {
        Preconditions.checkArgument(customExample != null, "Custom example object must not be null");
        return this.dao.findByExampleCustom(customExample);
    }

    /** {@inheritDoc} */
    @Override
    public List<Dom> findByExampleCustom(Example customExample, ResultCriteria resultCriteria) {
        return this.dao.findByExampleCustom(customExample, resultCriteria);
    }

    //----------------------------------  OTHER METHODS ----------------------------------------------------------------
    /**
     * @throws IllegalStateException if dao is not set
     */
    protected void assertConfigured() {
        if (dao == null) {
            throw new IllegalStateException("dao is null");
        }
    }

    /**
     * tries to use dao.
     *
     * @return
     */
    public Dao useDao() {
        assertConfigured();
        return dao;
    }

    @Override
    public Dao getDao() {
        return dao;
    }

    @Override
    public void setDao(Dao dao) {
        this.dao = dao;
    }

}
