package com.nila.blog.common.utils

import org.modelmapper.ModelMapper
import org.modelmapper.config.Configuration
import org.modelmapper.convention.MatchingStrategies
import java.util.stream.Collectors


class Mapper {
    private val modelMapper: ModelMapper

    private fun createModelMapper(): ModelMapper {
        val mapper = ModelMapper()
        mapper.configuration
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE).matchingStrategy =
            MatchingStrategies.STRICT
        return mapper
    }

    fun <M, D> toDto(model: M, dClass: Class<D>?): D {
        return modelMapper.map(model, dClass)
    }

    fun <M, D> toDtoList(modelList: List<M>, dClass: Class<D>?): List<D> {
        return modelList.stream()
            .map { model: M -> modelMapper.map(model, dClass) }
            .collect(Collectors.toList())
    }

    fun <M, D> toModel(dto: D, mClass: Class<M>?): M {
        return modelMapper.map(dto, mClass)
    }

    fun <M, D> toModelList(dtoList: List<D>, mClass: Class<M>?): List<M> {
        return dtoList.stream()
            .map { dto: D -> modelMapper.map(dto, mClass) }
            .collect(Collectors.toList())
    }

    init {
        modelMapper = createModelMapper()
    }
}
