package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.entity.common.AbstractEntityForRelatedFromSource;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "foreign_language")
@AttributeOverride(name = "id", column = @Column(name = "foreign_language_id"))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForeignLanguage extends AbstractEntityForRelatedFromSource {

    @Column(name = "name")
    String name;

}
