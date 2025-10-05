import React, { memo } from 'react';
import classnames from 'classnames';
import Loading from '@/components/Loading/Loading'

import styles from './index.less';

interface IProps {
  className?: string;
}

export default memo<IProps>(function LazyLoading({ className }) {
  return <div className={classnames(className, styles.box)}>
    <Loading></Loading>
  </div>
})
