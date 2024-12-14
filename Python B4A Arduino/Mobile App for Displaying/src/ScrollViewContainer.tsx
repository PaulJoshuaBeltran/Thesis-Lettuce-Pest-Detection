import React, {PropsWithChildren} from 'react';
import {Box, RefreshControl, ScrollView} from '@gluestack-ui/themed';
import styles from './styles.ts';
import {useHeaderHeight} from '@react-navigation/elements';
import {useBottomTabBarHeight} from '@react-navigation/bottom-tabs';
import {SafeAreaView} from 'react-native-safe-area-context';

function ScrollViewContainer({children}: PropsWithChildren): React.JSX.Element {
  const [refreshing, setRefreshing] = React.useState(false);

  const handleOnRefresh = React.useCallback(() => {
    setRefreshing(true);
    setTimeout(() => {
      setRefreshing(false);
    }, 2000);
  }, []);

  let headerHeight = useHeaderHeight();

  const renderRefreshControl = () => (
    <RefreshControl
      refreshing={refreshing}
      onRefresh={handleOnRefresh}
      progressViewOffset={headerHeight}
    />
  );

  let bottomTabBarHeight = useBottomTabBarHeight();

  return (
    <SafeAreaView edges={['right', 'left']} style={styles.container}>
      <ScrollView
        refreshControl={renderRefreshControl()}
        showsHorizontalScrollIndicator={false}
        showsVerticalScrollIndicator={false}>
        <Box
          style={{marginTop: headerHeight, paddingBottom: bottomTabBarHeight}}>
          {children}
        </Box>
      </ScrollView>
    </SafeAreaView>
  );
}

export default ScrollViewContainer;
