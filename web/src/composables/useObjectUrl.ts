import { onScopeDispose, ref, unref, watch, type MaybeRef } from 'vue';

export function useObjectUrl(source: MaybeRef<Blob | null | undefined>) {
  const objectUrl = ref<string | null>(null);

  const revoke = () => {
    if (objectUrl.value) {
      URL.revokeObjectURL(objectUrl.value);
      objectUrl.value = null;
    }
  };

  watch(
    () => unref(source),
    (blob) => {
      revoke();

      if (blob) {
        objectUrl.value = URL.createObjectURL(blob);
      }
    },
    { immediate: true }
  );

  onScopeDispose(revoke);

  return objectUrl;
}

