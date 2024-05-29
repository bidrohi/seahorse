package com.bidyut.tech.seahorse.annotation

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
    AnnotationTarget.FILE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
)
@Retention(AnnotationRetention.SOURCE)
@SeahorseInternalApi
annotation class SeahorseInternalApi
