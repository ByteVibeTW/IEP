<template>
  <div
    :class="[
      'mx-auto',
      'w-full',
      'max-w-[min(1200px,calc(100vw-1.5rem))]',
      'px-4',
      'sm:px-6',
      'lg:px-8',
      'py-6',
      'sm:py-8',
      'lg:py-10',
    ]"
  >
    <slot />
  </div>
</template>
